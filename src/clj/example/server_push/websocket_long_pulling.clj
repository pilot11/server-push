(ns example.server-push.websocket-long-pulling
  "websocket与长轮询服务器样例,服务器部分两种模式都支持,前端需要指定具体的模式
  主要使用com.taoensso/sente库实现,参考链接:
  https://github.com/ptaoussanis/sente"
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.core.async :refer [go-loop timeout <!]]))

(let [{:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket! (get-sch-adapter) {:user-id-fn :client-id})]
  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; 接收消息的通道
  (def chsk-send!                    send-fn) ; 发送消息的函数
  (def connected-uids                connected-uids)) ; 只读的 atom ,管理连接的id

; 可以查看连接,连接改变时输出
(add-watch connected-uids :connected-uids
           (fn [_ _ old new]
             (when (not= old new)
               (clojure.pprint/pprint new))))

(defn push
  "推送给指定client,data有格式要求,必须是一个vector,第一个元素为带命名空间的keyword"
  [uid data]
  (chsk-send! uid data))

(defn broadcast!
  "广播消息"
  [data]
  (doseq [uid (:any @connected-uids)]
    (push uid data)))

(defn run-broadcast
  "执行广播"
  []
  (go-loop []
    (<! (timeout 1000))
    (broadcast! [:server/broadcast (str (java.util.Date.))])
    (recur)))

(run-broadcast)

(defroutes routes
           (GET  "/ws" req (ring-ajax-get-or-ws-handshake req))
           (POST "/ws" req (ring-ajax-post req)))
