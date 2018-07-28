(ns example.server-push.ajax
  "常用的ajax请求处理服务器样例"
  (:require [compojure.core :refer [defroutes GET]]))

(defroutes routes
           (GET "/ajax" req (fn [req] (str (java.util.Date.)))))
