;; Copyright (c) 2013-2017, Kenneth Leung. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns ^{:doc ""
      :author "Kenneth Leung"}

  czlab.wabbit.common.core

  (:require [clojure.string :as cs]
            [clojure.java.io :as io])

  (:import [java.io File]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;(set! *warn-on-reflection* true)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^String c-verprops "czlab/wabbit/common/version.properties")
(def ^String c-rcb "czlab.wabbit.common/Resources")

(def ^:private ^String sys-devid-pfx "system.####")
(def ^:private ^String sys-devid-sfx "####")

(def sys-devid-regex #"system::[0-9A-Za-z_\-\.]+" )
(def shutdown-devid #"system::kill_9" )
(def ^String dft-dbid "default")

(def ^String shutdown-uri "/kill9")
(def ^String pod-protocol  "pod:" )
(def ^String meta-inf  "META-INF" )
(def ^String web-inf  "WEB-INF" )

(def ^String dn-target "target")
(def ^String dn-build "build")

(def ^String dn-classes "classes" )
(def ^String dn-bin "bin" )
(def ^String dn-conf "conf" )
(def ^String dn-lib "lib" )

(def ^String dn-cfgapp "etc/app" )
(def ^String dn-cfgweb "etc/web" )
(def ^String dn-etc "etc" )

(def ^String dn-rcprops  "Resources_en.properties" )
(def ^String dn-templates  "templates" )

(def ^String dn-logs "logs" )
(def ^String dn-tmp "tmp" )
(def ^String dn-dbs "dbs" )
(def ^String dn-dist "dist" )
(def ^String dn-views  "htmls" )
(def ^String dn-pages  "pages" )
(def ^String dn-patch "patch" )
(def ^String dn-media "media" )
(def ^String dn-scripts "scripts" )
(def ^String dn-styles "styles" )
(def ^String dn-pub "public" )

(def ^String web-classes  (str web-inf  "/" dn-classes))
(def ^String web-lib  (str web-inf  "/" dn-lib))
(def ^String web-log  (str web-inf  "/logs"))
(def ^String web-xml  (str web-inf  "/web.xml"))

(def ^String mn-rnotes (str meta-inf "/" "RELEASE-NOTES.txt"))
(def ^String mn-readme (str meta-inf "/" "README.md"))
(def ^String mn-notes (str meta-inf "/" "NOTES.txt"))
(def ^String mn-lic (str meta-inf "/" "LICENSE.txt"))

(def ^String pod-cf  "pod.conf" )
(def ^String cfg-pod-cf  (str dn-conf  "/"  pod-cf ))

(def jslot-flatline :____flatline)
(def evt-opts :____eventoptions)
(def jslot-last :____lastresult)
(def jslot-cred :credential)
(def jslot-user :principal)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defmacro gtid "typeid of component" [obj] `(:typeid (meta ~obj)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn getHomeDir
  ""
  ^File [] (io/file (System/getProperty "wabbit.home.dir")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defn getProcDir
  ""
  ^File [] (io/file (System/getProperty "wabbit.proc.dir")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF


