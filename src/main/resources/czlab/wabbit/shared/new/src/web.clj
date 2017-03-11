;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;auto-generated

(ns ^{:doc ""
      :author "{{user}}"}

  {{domain}}.core

  (:require [czlab.basal.logging :as log])

  (:use [czlab.convoy.net.core]
        [czlab.basal.core]
        [czlab.basal.str]
        [czlab.flux.wflow.core])

  (:import [czlab.flux.wflow Job Activity Workstream]
           [czlab.convoy.net RouteInfo HttpResult]
           [czlab.wabbit.plugs.io HttpMsg]
           [czlab.wabbit.sys Execvisor]))

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
(defn dftHandler "" []
  #(let
     [^HttpMsg evt (.origin ^Job %)
      gist (.msgGist evt)
      co (. evt source)
      ^RouteInfo
      ri (get-in gist
                 [:route :info])
      tpl (some-> ri .template)
      {:keys [data ctype]}
      (if (hgl? tpl)
        (loadTemplate co tpl (ftlContext)))
      res (httpResult<> evt)]
     (.setContentType res ctype)
     (.setContent res data)
     (replyResult res (.config co))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn myAppMain
  ""
  []
  (log/info "My AppMain called!"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF

