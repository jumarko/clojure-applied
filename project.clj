(defproject clojure-applied "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [prismatic/schema "1.1.2"]
                 [medley "0.8.2"]]
  :main ^:skip-aot clojure-applied.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
