(ns example.server-push
 "服务集成"
 (:require [compojure.core :refer [routes defroutes]]
           [ring.middleware.keyword-params :refer [wrap-keyword-params]]
           [ring.middleware.params :refer [wrap-params]]
           [example.server-push.ajax :as ajax]
           [example.server-push.sse :as sse]
           [example.server-push.websocket-long-pulling :as websocket]))

(def handler
  "前端请求处理,组合了几种推送方式的分发路由"
  (-> (routes
        ajax/routes
        sse/routes
        websocket/routes)
      wrap-keyword-params
      wrap-params))
