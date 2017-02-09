;; Copyright (c) 2013-2017, Kenneth Leung. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns czlab.wabbit.shared.new

  (:import [java.util.concurrent.atomic AtomicInteger]
           [java.rmi.server UID]
           [java.util UUID])

  (:require [czlab.wabbit.shared.templates :as lein]
            [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [clojure.string :as cs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(def ^:dynamic *template-name*
  (or "wabbit"
      (last (cs/split (str *ns*) #"\."))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(def ^:private template-files
  {"conf/pod.conf" "pod.conf"
   "etc"
   {"Resources_en.properties" identity
    "mime.properties" identity
    "log4j2c.xml" identity
    "log4j2d.xml" identity
    "shiro.ini" identity}
   "src/main/clojure/{{nested-dirs}}"
   {"core.clj" "src/soa.clj"}
   "src/main/java/{{nested-dirs}}"
   {"HelloWorld.java" "src/HelloWorld.java"}
   "src/test/clojure/{{nested-dirs}}/test"
   {"test.clj" "src/test.clj"}
   "src/test/java/{{nested-dirs}}/test"
   {"ClojureJUnit.java" "src/ClojureJUnit.java"
    "JUnit.java" "src/JUnit.java"}
   "src/web/media"
   {"favicon.png" "web/favicon.png"
    "favicon.ico" "web/favicon.ico"}
   "src/web/pages" {"index.html" "web/index.html"}
   "src/web/scripts" {"main.js" "web/main.js"}
   "src/web/styles" {"main.scss" "web/main.scss"}
   ".gitignore" "gitignore"
   "CHANGELOG.md" identity
   "doc" {"intro.md" "intro.md"}
   "LICENSE" identity
   "project.clj" identity
   "README.md" identity
   "public" {}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn- isWindows?
  "Is platform Windows?" []
  (>= (.indexOf (cs/lower-case (System/getProperty "os.name")) "windows") 0))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn- stripLS "" [s] (cs/replace s #"^[/]+" ""))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn- juid "" [] (.replaceAll (str (UID.)) "[:\\-]+" ""))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defmacro ^:private explodePath "" [s] `(remove empty? (cs/split ~s #"/")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defmacro ^:private sanitizePath "" [s] `(cs/join "/" (explodePath ~s)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn- render "" [path data]
  (if (some #(.endsWith ^String path ^String %)
            [".png" ".ico" ".jpg" ".gif"])
    ((lein/rawResourcer *template-name*) path)
    ((lein/renderer *template-name*) path data)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn- doRender
  "" [tfiles data out]
  (doseq [t tfiles]
    (if (string? t)
      (swap! out conj t)
      (let [[k v] t]
        (swap! out conj [k (render v data)])))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn- traverse
  "" [par tfiles out]
  (doseq [[k v] tfiles
          :let [kk (sanitizePath (str par "/" k))]]
    (cond
      (map? v)
      (if (empty? v)
        (swap! out conj kk)
        (traverse kk v out))
      (fn? v)
      (swap! out conj [kk (v kk)])
      (string? v)
      (swap! out conj [kk v]))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn new<>
  "A lein template for creating a czlab/wabbit application"
  [name options & args]
  (let
    [args (if (empty? args) ["-web"] args)
     render-fn (:renderer-fn options)
     main-ns (lein/sanitizeNsp name)
     pod (last (cs/split main-ns #"\."))
     uid (str (UUID/randomUUID))
     h2dbUrl (->
               (cs/join "/"
                        [(if (isWindows?)
                           "/c:/temp" "/tmp")
                         (juid)
                         pod])
               (str ";MVCC=TRUE;AUTO_RECONNECT=TRUE"))
     data {:user (System/getProperty "user.name")
           :nested-dirs (lein/nameToPath main-ns)
           :app-key (cs/replace uid #"-" "")
           :h2dbpath h2dbUrl
           :domain main-ns
           :raw-name name
           :project pod
           :ver "0.1.0"
           :name name
           :year (lein/year)
           :date (lein/date)}
     out2 (atom [])
     out (atom [])]
    (binding [lein/*renderer-fn* render-fn]
      (traverse "" template-files out)
      (doRender @out data out2)
      (apply lein/toFiles
             (dissoc options :renderer-fn) data @out2))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF

