(ns example.server-push.websocket-long-pulling
  "websocket与长轮询的网页样例
  使用sente库实现的websocket与long-pulling过程是一样的,
  只是在网页端的 make-channel-socket-client! 函数参数中指定是使用 :ws 或 :ajax
  但在服务集成上有所区别,websocket要求服务协议是https的,long-pulling则是http和https均可"
  (:require [taoensso.sente :as sente  :refer (cb-success?)]
            [taoensso.encore :refer-macros (have have?)]))

(defmulti handler
          "消息处理分发函数"
          :id)

(defmethod handler :chsk/recv
  [{:keys [?data]} db]
  (swap! db assoc :long-polling (second ?data)))

(defmethod handler :default
  [{:keys [event]} db]
  #_(.log js/console (str "Unhandled event: " event)))

(defn mk-event-handler
  "构造消息事件的处理函数"
  [db]
  (fn [event-msg]
    #_(.log js/console (str "event-msg: " event-msg))
    (handler event-msg db)))

(defn run
  "执行连接,接收消息"
  [db]
  (let [{:keys [ch-recv ; 接收消息的通道
                state]} ; 只读的atom
        (sente/make-channel-socket-client! "/ws"
          {:type :ajax ; 这里可以指定使用的协议: :ws 为websocket, :ajax为长轮询, :auto为自动选择这两者之一
           :client-id "3196232d-0b92-4746-bc5a-6a46d8102c47"})] ;这里可以指定连接的id,服务可以根据此id进行推送
    (println (str "state: " @state)) ;查看state
    (sente/start-client-chsk-router! ch-recv (mk-event-handler db))))
