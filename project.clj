;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defproject io.czlab/wabbit-shared "0.1.0"

  :license {:url "http://www.eclipse.org/legal/epl-v10.html"
            :name "Eclipse Public License"}
  :url "https://github.com/llnek/wabbit-shared"

  :description "Sharable parts of wabbit."

  :plugins [[lein-codox "0.10.2"]
            [lein-czlab "0.1.1"]
            [lein-pprint "1.1.2"]]
  :hooks [leiningen.lein-czlab]

  ;;:dependencies []

  :profiles {:provided {:dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                                       [codox/codox "0.10.2" :scope "provided"]]}
             :uberjar {:aot :all}}

  :global-vars {*warn-on-reflection* true}
  :coordinate! "czlab/wabbit/shared"
  :target-path "out/%s"
  :omit-source true
  :aot :all

  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :resource-paths ["src/main/resources"])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF

