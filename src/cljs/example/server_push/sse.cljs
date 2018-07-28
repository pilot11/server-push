(ns example.server-push.sse
  "server-sent-events网页部分样例"
  (:require [ajax.core :refer [ajax-request text-response-format]]))

(defn mk-request-handler
  "构造请求响应的处理函数,处理函数的参数列表格式是固定的"
  [db]
  (fn [event]
    (swap! db assoc :sse (aget event "data"))))

(defn run
  "执行"
  [db]
  (let [source (new js/EventSource "/sse")]
    (aset source "onmessage" (mk-request-handler db))))
