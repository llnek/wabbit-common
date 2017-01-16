;; Copyright (c) 2013-2017, Kenneth Leung. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns ^{:doc "Built-in wabbit emitter definitions."
      :author "Kenneth Leung"}

  czlab.wabbit.shared.svcs)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;(set! *warn-on-reflection* true)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(def ^:private emitter-svcs
  {:czlab.wabbit.io.files/FilePicker
   {:info {:version "1.0"
           :type :files
           :name "File Picker"}
    :conf {:comment# "place comments here"
           :targetFolder "/home/dropbox"
           :recvFolder "/home/joe"
           :fmask ""
           :intervalSecs 300
           :delaySecs 0
           :handler ""}}

   :czlab.wabbit.io.mails/IMAP
   {:info {:version "1.0"
           :type :imap
           :name "IMAP Client"}
    :conf {:comment# "place comments here"
           :host "imap.gmail.com"
           :port 993
           :deleteMsg? false
           :ssl? true
           :username "joe"
           :passwd "secret"
           :intervalSecs 300
           :delaySecs 0
           :handler ""}}

   :czlab.wabbit.io.jms/JMS
   {:info {:version "1.0"
           :type :jms
           :name "JMS Client"}
    :conf {:contextFactory "czlab.wabbit.mock.jms.MockContextFactory"
           :comment# "place comments here"
           :providerUrl "java://aaa"
           :connFactory "tcf"
           :destination "topic.abc"
           :jndiUser "root"
           :jndiPwd "root"
           :jmsUser "anonymous"
           :jmsPwd "anonymous"
           :handler ""}}

   :czlab.wabbit.io.http/HTTP
   {:info {:version "1.0"
           :type :http
           :name "HTTP Server"}
    :conf {:comment# "place comments here"
           :maxInMemory (* 1024 1024 4)
           :maxContentSize -1
           :waitMillis 0
           :sockTimeOut 0
           :host ""
           :port 9090
           :serverKey ""
           :passwd ""
           :handler ""
           :routes nil
           :routes_example
           [{:handler ""
             :uri "/get"
             :verbs #{:get}}
            {:handler ""
             :uri "/post"
             :verbs #{:post :put}}]}}

   :czlab.wabbit.io.http/WebMVC
   {:info {:version "1.0"
           :type :web
           :name "Web Site"}
    :conf {:comment# "place comments here"
           :maxInMemory (* 1024 1024 4)
           :maxContentSize -1
           :waitMillis 0
           :sockTimeOut 0
           :host ""
           :port 9090
           :serverKey ""
           :passwd ""
           :sessionAgeSecs 2592000
           :maxIdleSecs 0
           :hidden true
           :domain ""
           :domainPath "/"
           :maxAgeSecs 3600
           :useETags? false
           :errorHandler ""
           :handler ""
           :routes nil
           :routes_example
           [{:mount "${pod.dir}/public/media/main/{}"
             :uri "/(favicon\\..+)"}
            {:mount "${pod.dir}/public/{}"
             :uri "/public/(.*)"}
            {:handler ""
             :uri "/?"
             :verbs #{:get}
             :template  "/main/index.html"}]}}

   :czlab.wabbit.io.loops/OnceTimer
   {:info {:version "1.0"
           :type :once
           :name "One Shot Timer"}
    :conf {:comment# "place comments here"
           :delaySecs 0
           :handler ""}}

   :czlab.wabbit.io.mails/POP3
   {:info {:version "1.0"
           :type :pop3
           :name "POP3 Client"}
    :conf {:comment# "place comments here"
           :host "pop.gmail.com"
           :port 995
           :deleteMsg? false
           :username "joe"
           :passwd "secret"
           :intervalSecs 300
           :delaySecs 0
           :ssl? true
           :handler ""}}

   :czlab.wabbit.io.loops/RepeatingTimer
   {:info {:version "1.0"
           :type :repeater
           :name "Repeating Timer"}
    :conf {:comment# "place comments here"
           :intervalSecs 300
           :delaySecs 0
           :handler ""}}

   :czlab.wabbit.io.socket/Socket
   {:info {:version "1.0"
           :type :tcp
           :name "TCP Socket Server"}
    :conf {:comment# "place comments here"
           :host ""
           :port 7551
           :handler ""}}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(def ^:private emitter-types
  (persistent!
    (reduce
      #(let [[k v] %2
             t (get-in v [:info :type])
             v' (update-in v
                           [:conf]
                           assoc :service k)]
         (assoc! %1 t v'))
      (transient {})
      emitter-svcs)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn emitterByService
  ""
  [svc]
  (get emitter-svcs svc))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn emitterByType
  ""
  [ty]
  (get emitter-types ty))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn emitterServices
  ""
  []
  (doto emitter-svcs))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn emitterTypes
  ""
  []
  (doto emitter-types))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF

