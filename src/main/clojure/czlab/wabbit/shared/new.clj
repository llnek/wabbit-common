;; Copyright © 2013-2019, Kenneth Leung. All rights reserved.
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
(def ^:dynamic *template-name*
  (or "wabbit"
      (last (cs/split (str *ns*) #"\."))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;left hand side is the target, right side is from the resource path -
;;from the resource files, generate a target dir structure.
(def
  ^:private
  template-files
  {"conf/pod.conf" "pod.conf"

   "etc"
   {"Resources_en.properties" identity
    "mime.properties" identity
    "log4j2c.xml" identity
    "log4j2d.xml" identity
    "shiro.ini" identity}

   "src/main/clojure/{{nested-dirs}}"
   {"core.clj" "src/{{app-type}}.clj"}

   "src/main/java/{{nested-dirs}}"
   {"Bonjour.java" "src/Bonjour.java"}

   "src/test/clojure/{{nested-dirs}}/test"
   {"test.clj" "src/test.clj"}

   ;;"src/test/java/{{nested-dirs}}/test"
   ;;{"ClojureJUnit.java" "src/ClojureJUnit.java"
   ;;"JUnit.java" "src/JUnit.java"}

   "public/res/main"
   {"favicon.png" "web/favicon.png"
    "favicon.ico" "web/favicon.ico"}

   "public/htm/main"
   {"index_p1.ftl" "web/index_p1.ftl"
    "index_p2.ftl" "web/index_p2.ftl"
    "index_p3.ftl" "web/index_p3.ftl"
    "index_p4.ftl" "web/index_p4.ftl"
    "index.html" "web/index.html"}

   "public/jsc/main"
   {"main.js" "web/main.js"}

   "public/css/main"
   {"main.css" "web/main.scss"}

   ".gitignore" "gitignore"
   "CHANGELOG.md" identity

   "doc" {"intro.md" "intro.md"}

   "LICENSE" identity
   "project.clj" identity
   "README.md" identity

   "public" {}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- is-windows?
  "Is platform Windows?" []
  (number? (cs/index-of
             (cs/lower-case (System/getProperty "os.name")) "windows")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- strip-ls
  "" [s] (cs/replace s #"^[/]+" ""))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- juid
  "" [] (.replaceAll (str (UID.)) "[:\\-]+" ""))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmacro ^:private
  explode-path "" [s] `(remove empty? (cs/split ~s #"/")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmacro ^:private
  sanitize-path "" [s] `(cs/join "/" (explode-path ~s)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- render
  "" [path data]
  (if (some #(cs/ends-with? path %)
            [".png" ".ico" ".jpg" ".gif"])
    ((lein/raw-resourcer *template-name*) path)
    ((lein/renderer *template-name*) path data)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- do-render
  "" [tfiles data]
  (loop [out []
         [t & more] tfiles]
    (if (nil? t)
      out
      (recur
        (if (string? t)
          (conj out t)
          (conj out [(first t) (render (last t) data)])) more))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- traverse
  "Traverse the resource template."
  ([tfiles] (traverse "" tfiles))
  ([par tfiles]
   (loop [out []
          [t & more] (seq tfiles)]
     (if (nil? t)
       out
       (let [[k v] t
             kk (sanitize-path (str par "/" k))]
        (recur
          (cond (map? v)
                (if (empty? v)
                  (conj out kk)
                  (concat out (traverse kk v)))
                (fn? v)
                (conj out [kk (v kk)])
                (string? v)
                (conj out [kk v])
                :else
                (throw (Exception.
                         (str "Unsupported resource: " k)))) more))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn new<>
  "Lein template for creating
  a czlab/wabbit application." [name options & args]

  (let
    [args (if (empty? args) ["-web"] args)
     web? (cs/index-of (cs/lower-case
                         (str (first args))) "web")
     render-fn (:renderer-fn options)
     main-ns (lein/sanitize-nsp name)
     pod (last (cs/split main-ns #"\."))
     uid (str (UUID/randomUUID))
     h2dbUrl (str (->> [(if (is-windows?)
                          "/c:/windows/temp" "/tmp") (juid) pod]
                       (cs/join "/"))
                  ";MVCC=TRUE;AUTO_RECONNECT=TRUE")
     data {:nested-dirs (lein/name->path main-ns)
           :h2dbpath h2dbUrl
           :domain main-ns
           :raw-name name
           :project pod
           :ver "0.1"
           :name name
           :year (lein/year)
           :date (lein/date)
           :app-key (cs/replace uid #"-" "")
           :app-type (if web? "web" "server")
           :user (System/getProperty "user.name")}]
    (binding [lein/*renderer-fn* render-fn]
      (apply lein/x->files
             (dissoc options :renderer-fn)
             data
             (-> (traverse "" template-files) (do-render data))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF

