(defproject app ""
  
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3126"]
                 [figwheel "0.2.5"]]
  
  :plugins [[lein-cljsbuild "1.0.5"]
            [lein-figwheel "0.2.5"]]
  
  :hooks [leiningen.cljsbuild]
  
  :figwheel {:css-dirs ["resources/public/styles"]}
  
  :clean-targets ^{:protect false} ["resources/public/scripts"]
  
  :cljsbuild {:builds {:main {:source-paths ["source"]
                              :compiler { :output-to "resources/public/scripts/scripts.js"
                                          :output-dir "resources/public/scripts"
                                          :compiler-stats true}}}}
  
  :profiles {
    ; lein figwheel
    :dev { :cljsbuild {:builds {:main { :source-paths ["dev"]
                                        :compiler { :main dev.dev
                                                    :asset-path "scripts"
                                                    :optimizations :none
                                                    :source-map-timestamp true}}}}}
    ; lein with-profile prod compile
    :prod {:cljsbuild {:builds {:main { :compiler { :optimizations :advanced
                                                    :source-map "resources/public/scripts/scripts.js.map"
                                                    :elide-asserts true
                                                    :pretty-print false}}}}}})
