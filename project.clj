(defproject server-push "1.0.0-SNAPSHOT"
  :source-paths ["src/clj" "src/cljs"]
  :dependencies [
                 ; 服务部分
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.4.474"]
                 [ring/ring-core "1.6.3"]
                 [compojure "1.6.1"]
                 [com.taoensso/sente "1.12.0"]
                 [http-kit "2.3.0"]

                 ; 网页部分
                 [org.clojure/clojurescript "1.10.339"]
                 [reagent "0.8.1"]
                 [cljs-ajax "0.7.4"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.16"]]

  :figwheel {:server-port 8080
             :ring-handler example.server-push/handler
             :http-server-root "public"
             :css-dirs ["resources/public/css"]}

  :cljsbuild {:builds [{:id "example"
                        :source-paths ["src/cljs"]
                        :figwheel     true
                        :compiler     {:main          "example.server-push"
                                       :asset-path    "js/example"
                                       :output-to     "resources/public/js/example.js"
                                       :output-dir    "resources/public/js/example"
                                       :optimizations :none
                                       :pretty-print  true}}]}

  :repositories [["clojars" {:url "https://repo.clojars.org/"}]]
  )

; 使用命令 lein figwheel example 启动调试环境
