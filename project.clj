;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defproject io.czlab/wabbit-svcs "0.1.0"

  :description ""
  :url "https://github.com/llnek/wabbit-svcs"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :coordinate! "czlab/wabbit/svcs"

  :plugins [[lein-codox "0.10.2"]
            [lein-czlab "0.1.1"]]
  :hooks [leiningen.lein-czlab]

  ;;:dependencies [[org.clojure/clojure "1.8.0"]]

  :profiles {:provided {:dependencies
                        [[org.clojure/clojure "1.8.0" :scope "provided"]
                         [codox/codox "0.10.2" :scope "provided"]]}
             :uberjar {:aot :all}}

  :global-vars {*warn-on-reflection* true}
  :target-path "out/%s"
  :aot :all
  :omit-source true

  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]

  :resource-paths ["src/main/resources"])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF

