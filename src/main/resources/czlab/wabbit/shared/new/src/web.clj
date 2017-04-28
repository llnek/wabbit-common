;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;auto-generated

(ns ^{:doc ""
      :author "{{user}}"}

  {{domain}}.core

  (:use [czlab.convoy.core]
        [czlab.basal.core]
        [czlab.basal.str])

  (:import [java.io File]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn- ftlContext
  ""
  []
  {:landing
             {:title_line "Sample Web App"
              :title_2 "Demo wabbit"
              :tagline "Say something" }
   :about
             {:title "About wabbit demo" }
   :services {}
   :contact {:email "a@b.com"}
   :description "wabbit web app"
   :encoding "utf-8"
   :title "wabbit|Sample"})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn dftHandler "" [evt res]
  (do-with
    [ch (:socket evt)]
    (let
      [plug (get-pluglet evt)
       svr (get-server plug)
       ri (get-in evt
                  [:route :info])
       tpl (:template ri)
       {:keys [data ctype]}
       (if (hgl? tpl)
         (loadTemplate svr tpl (ftlContext)))]
      (->>
        (-> (set-res-header ch res "content-type" ctype)
            (assoc :body data))
        (reply-result ch )))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn myAppMain "" [svr]
  (println  "My AppMain called!"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF

