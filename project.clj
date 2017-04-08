(defproject hnalyze "0.1.0-SNAPSHOT"
  :description "A Clojure program for analyzing HN users who dominate discussion."
  :url "https://github.com/MatthewDarling/HNAlyze"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [cheshire "5.7.0"]]
  :main ^:skip-aot hnalyze.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
