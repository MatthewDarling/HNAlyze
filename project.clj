(defproject hnalyze "0.1.0-SNAPSHOT"
  :description "A Clojure program for analyzing HN users who dominate discussion."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/tools.cli "0.3.1"]
                 [cheshire "5.3.1"]]
  :main ^:skip-aot hnalyze.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
