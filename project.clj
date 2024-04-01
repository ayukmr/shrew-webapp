(defproject shrew "0.1.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/clojurescript "1.11.132"]
                 [clj-commons/secretary "1.2.4"]
                 [rum "0.12.11"]
                 [reagent-utils "0.3.8"]
                 [cljs-http "0.1.48"]]

  :plugins [[lein-cljsbuild "1.1.8"]]

  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:output-to  "public/build/main.js"
                                   :output-dir "public/build"
                                   :optimizations :simple
                                   :source-map "public/build/main.js.map"}}]})
