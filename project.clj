;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
(defproject io.czlab/wabbit-shared "1.0.0"

  :license {:url "http://www.eclipse.org/legal/epl-v10.html"
            :name "Eclipse Public License"}
  :url "https://github.com/llnek/wabbit-shared"

  :description "Sharable parts of wabbit."

  :dependencies [[commons-io/commons-io "2.5"]]

  :plugins [[lein-codox "0.10.2"]
            [lein-pprint "1.1.2"]]

  :profiles {:provided {:dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                                       [codox/codox "0.10.2" :scope "provided"]]}
             :uberjar {:aot :all}}

  :global-vars {*warn-on-reflection* true}
  :coordinate! "czlab/wabbit/shared"
  :target-path "out/%s"
  :omit-source true
  :aot :all

  :java-source-paths ["src/main/java"]
  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :resource-paths ["src/main/resources"])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;EOF

