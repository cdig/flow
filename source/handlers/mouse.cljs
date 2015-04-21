;; Maps browser mouse events into nice data

;; Side Effects √
;; State √
;; Deps √

(ns handlers.mouse
  (:require [app.db :as db]
            [app.events :as events]
            [browser.mouse :as mouse]))

(defn setup! []
  
  (db/set-cache! ::abs {:x 0 :y 0})
  (db/set-cache! ::delta {:x 0 :y 0})
  (db/set-cache! ::down? false)
  
  (events/register-event-handler!
    "mousemove"
    ;; Handler
    (fn [event]
      (let [type (if (db/get-cache ::down?) :mouse-drag :mouse-move)
            abs (mouse/event->pos event)
            rel (merge-with - abs (db/get-cache ::abs))
            delta (merge-with + rel (db/get-cache ::delta))]
        (when-not (and (zero? (:x rel))
                       (zero? (:y rel)))
          (db/set-cache! ::abs abs)
          (db/set-cache! ::delta delta)
          [type {:abs abs
                 :rel rel
                 :delta delta}])))
    ;; Merger
    (fn [older newer]
      (assoc newer :rel (merge-with + (:rel older) (:rel newer)))))

  (events/register-event-handler!
    "mousedown"
    ;; Handler
    (fn [event]
      (db/set-cache! ::down? true)
      (db/set-cache! ::delta {:x 0 :y 0})
      [:mouse-down {:down true}])
    ;; Merger
    (fn [older newer]
      newer))
      
  (events/register-event-handler!
    "mouseup"
    ;; Handler
    (fn [event]
      (db/set-cache! ::down? false)
      (db/set-cache! ::delta {:x 0 :y 0})
      [:mouse-up {:down false}])
    ;; Merger
    (fn [older newer]
      newer)))
