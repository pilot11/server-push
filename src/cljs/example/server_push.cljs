(ns example.server-push
  "集成"
  (:require [reagent.core :as r]
            [example.server-push.sse :as sse]
            [example.server-push.ajax :as ajax]
            [example.server-push.websocket-long-pulling :as websocket]))

(def db
  "数据,服务器推送过来的数据存放到这里,reagent将这些数据的内容渲染到网页上"
  (r/atom {:ajax "ajax"
           :long-polling "long-polling"
           :websocket "websocket"
           :sse "sse"}))

(defn result-component
  "显示结果的网页组件"
  [title body]
  [:div.result
   [:h3 title]
   [:p body]])

(defn view
  "集成组件"
  [db]
  [:div
   [result-component "ajax查询:" (:ajax @db)]
   [result-component "长轮询:" (:long-polling @db)]
   [result-component "websocket推送:" (:websocket @db)]
   [result-component "sse推送:" (:sse @db)]])

(defn ^:export -main
  []
  (ajax/run db)
  (websocket/run db)
  (sse/run db)
  (r/render [view db] (.getElementById js/document "app")))

(-main)