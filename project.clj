(defproject re-frame-lambda-io-sp-2016 "0.1.0-SNAPSHOT"
  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async "0.2.391"
                  :exclusions [org.clojure/tools.reader]]

                 [reagent "0.6.0"]                ; rice
                 [re-frame "0.8.0"]               ; beans
                 [re-com "1.2.0"]                 ; ui components
                 [re-frisk "0.3.1"]               ; visualizing current db

                 [day8.re-frame/http-fx "0.1.3"]] ; for http requests

  :plugins [[lein-figwheel "0.5.8"]
            [lein-cljsbuild "1.1.4" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {:builds
              [;; dev build - uses figwheel
               {:id "dev"
                :source-paths ["src"]

                :figwheel {:on-jsload "lambda-io.core/on-js-reload"
                           :open-urls ["http://localhost:3449/index.html"]}

                :compiler {:main lambda-io.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/lambda_io.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}

               ;; minified build
               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/lambda_io.js"
                           :main lambda-io.core
                           :optimizations :advanced
                           :pretty-print false}}]}

  :figwheel {:css-dirs ["resources/public/css"] ;; watch and update CSS
             :nrepl-port 7888}

  :profiles {:dev {:dependencies [[binaryage/devtools "0.8.2"]
                                  [figwheel-sidecar "0.5.8"]
                                  [com.cemerick/piggieback "0.2.1"]]

                   ;; need to add dev source path here to get user.clj loaded
                   :source-paths ["src" "dev"]
                   :repl-options {; for nREPL dev you really need to limit output
                                  :init (set! *print-length* 50)
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})


