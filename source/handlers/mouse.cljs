;; Maps browser mouse events into nice data

;; Side Effects √
;; State X
;; Deps √

(ns handlers.mouse
  (:require [app.db :as db]
            [app.events :as events]
            [browser.mouse :as mouse]))

(defn initialize! []

  (events/register-event-handler!
    "mousemove"
    (fn [event]
      (let [type (if (db/get-cache ::down?) :mouse-drag :mouse-move)
            abs (mouse/event->pos event)
            rel (merge-with - abs (db/get-cache ::abs))]
        (when-not (and (zero? (:x rel))
                       (zero? (:y rel)))
          (db/set-cache! ::abs abs)
          [type {:abs abs :rel rel}]))))

  (events/register-event-handler!
    "mousedown"
    (fn [event]
      (db/set-cache! ::down? true)
      [:mouse-down {:down true}]))

  (events/register-event-handler!
    "mouseup"
    (fn [event]
      (db/set-cache! ::down? false)
      [:mouse-up {:down false}])))

(defonce initialized (do (initialize!) true))
