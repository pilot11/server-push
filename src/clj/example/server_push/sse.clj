(ns example.server-push.sse
  "server-sent-events处理服务器样例"
  (:require [compojure.core :refer [defroutes GET]]
            [clojure.core.async :as a :refer [go-loop <! >! timeout]]
            [org.httpkit.server :refer [send! with-channel]]))

(def counter (atom 0))

(defn format-msg
  "格式化推送的数据,
  包含id event retry data域,域名与域值间以 : 分隔
  域间以\n分隔,一个事件必须以\n\n结尾"
  [data]
  (format "id:%d\nevent:%s\nretry:%d\ndata:%s\n\n"
          (swap! counter inc)
          "message"
          10000
          (str data)))

(defn sse-handler
  [req]
  (with-channel req channel
                (send! channel {:headers {"Content-Type" "text/event-stream;charset=utf-8"}}  false)
                (go-loop []
                  (<! (timeout 1000))
                  (send! channel (format-msg (java.util.Date.)) false)
                  (recur))))

(defroutes routes
           (GET "/sse" req sse-handler))
