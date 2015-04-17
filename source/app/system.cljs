(ns app.system
  (:require [app.db :as db]
            [app.messenger :as messenger]))

(defn register-system! [sname callback & messages]
  (doseq [message messages]
    (swap! db/cache assoc-in [:message->sname->callback message sname] callback)))

;; Call all system callbacks for the given message on every matching behaviour.
;; If their callback returns a value, set that as the new state of the behaviour.
(defn- update-systems [message message-data]
  (let [sname->callback (get-in @db/cache [:message->sname->callback message])]
    (doseq [[sname callback] sname->callback]
      (let [bids-for-sname (get-in @db/cache [:sname->bids sname])]
        (doseq [bid bids-for-sname]
          (let [behaviour (get-in @db/state [:behaviours bid])
                eid (get-in @db/cache [:bid->eid bid])
                entity (get-in @db/state [:entities eid])
                state (:state behaviour)
                result (apply callback entity state message-data)]
            (when result
              (swap! db/state assoc-in [:behaviours bid :state] result))))))))


(defn- initialize! []
  (messenger/register-transmitter! update-systems))

(defonce initialized (do (initialize!) true))
