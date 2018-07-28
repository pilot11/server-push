(ns example.server-push.ajax
  "使用ajax周期性向服务器查询"
  (:require [ajax.core :refer [ajax-request text-response-format]]))

(defn mk-request-handler
  "构造请求响应的处理函数,处理函数的参数列表格式是固定的"
  [db]
  (fn [[success? resp]]
    (if success?
      (swap! db assoc :ajax resp)
      (swap! db assoc :ajax "error!"))))

(defn request
  "向服务器请求"
  [uri handler]
  (ajax-request
    {:uri    uri
     :method  :get
     :handler handler
     :response-format (text-response-format)}))

(defn run
  "执行周期性查询"
  [db]
  (js-invoke js/window "setInterval"
             #(request "/ajax" (mk-request-handler db))
             1000))
