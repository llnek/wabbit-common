;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Original: https://github.com/technomancy/leiningen/src/leiningen/new/templates.clj
;;
(ns czlab.wabbit.shared.templates

  (:require [clojure.java.io :as io]
            [clojure.string :as cs])

  (:import [java.util Calendar]
           [java.io BufferedReader File]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; can't set this to stencil.core/render-string here because
;; pulling in the stencil lib in this library will cause
;; classloading issues when used by lein-wabbit as a
;; leiningen template.
;; this function should return back a string if using stencil
(def ^:dynamic *renderer-fn* nil)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmacro ^:private trap!
  "" [s] `(throw (Exception. (str ~s))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- fix-line-seps
  "" [s]
  (cs/replace s
              "\n"
              (if (System/getenv "LEIN_NEW_UNIX_NEWLINES")
                "\n" (System/getProperty "line.separator"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- slurp->lf
  [^BufferedReader r]
  (let [sb (StringBuilder.)]
    (loop [s (.readLine r)]
      (when s
        (.append sb s)
        (.append sb "\n")
        (recur (.readLine r)))) (str sb)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- slurp-resource
  "" [res]
  ; for 2.0.0 compatibility, can break in 3.0.0
  (-> (if (string? res)
        (io/resource res) res)
      io/reader slurp->lf fix-line-seps))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmacro sanitize
  "" [s] `(clojure.string/replace ~s "-" "_"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn name->path
  "" [s]
  (cs/replace (sanitize s) "." java.io.File/separator))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn sanitize-nsp
  "" [s]
  (-> (cs/replace s "/" ".") (cs/replace "_" "-")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn group-name
  "" [s]
  (let [grpseq (butlast (cs/split (sanitize-nsp s) #"\."))]
    (if (seq grpseq)
      (->> grpseq (interpose ".") (apply str)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn year
  "" [] (.get (Calendar/getInstance) Calendar/YEAR))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn date
  "" []
  (-> (java.text.SimpleDateFormat. "yyyy-MM-dd")
      (.format (java.util.Date.))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- res-path
  "" [path]
  (let [p (cs/join "/"
                   ["czlab/wabbit/shared/new" path])]
    [p (io/resource p)]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn renderer
  ""
  ([name] (renderer name nil))
  ([name render-fn]
   (let [render (or render-fn *renderer-fn*)]
     (fn [template & [data]]
       (let [template (if data
                        (render template data) template)
             [p r] (res-path template)]
         (if r
           (if data
             (render (slurp-resource r) data)
             (io/reader r))
           (trap! (format "Resource '%s' not found" p))))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn raw-resourcer
  "" [name]
  (fn [file]
    (let [[p r] (res-path file)]
      (if r
        (io/input-stream r)
        (trap! (format "Resource '%s' not found" p))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn- template-path
  "" ^File [name path data]
  (io/file name (*renderer-fn* path data)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn x->files
  "" [{:as cli-options
       :keys [dir force?]} {:as data :keys [name]} & paths]
  (if (or (= "." dir)
          (.mkdir (io/file dir)) force?)
    (doseq [path paths]
      (if (string? path)
        (.mkdirs (template-path dir path data))
        (let [[path content & options] path
              path (template-path dir path data)
              options (apply hash-map options)]
          (.mkdirs (.getParentFile path))
          (io/copy content (io/file path))
          (when (:executable options)
            (.setExecutable path true)))))
    (trap! (str "Could not create directory "
                dir
                ". Maybe it already exists?"
                "  See also :force or --force"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF


