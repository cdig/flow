;; Maps browser mouse events into nice data

;; Side Effects √
;; State √
;; Deps √

(ns handlers.mouse
  (:require [app.db :as db]
            [app.events :as events]
            [browser.mouse :as mouse]))

(def empty-pos {:x 0 :y 0})

;; HELPERS

(defn- has-moved?
  "Pass in a rel to see if the mouse has moved."
  [{:keys [x y]}]
  (not (and (zero? x)
            (zero? y))))

(defn- update-mouse-position!
  "Extract the new abs, rel, and delta positions from the mouse event. For perf, returns nil if there was no change since last call. When desired, we can reset the delta to 0, which always returns a new value, never nil."
  [event reset-delta?]
  (let [abs (mouse/event->pos event)
        rel (merge-with - abs (db/get-cache ::abs))
        delta (if reset-delta?
                  empty-pos
                  (merge-with + rel (db/get-cache ::delta)))]
    (when (or reset-delta? (has-moved? rel))
      (db/set-cache! ::abs abs)
      (db/set-cache! ::delta delta)
      {:abs abs
       :rel rel
       :delta delta})))

;; PUBLIC

(defn setup! []
  
  (db/set-cache! ::abs empty-pos)
  (db/set-cache! ::delta empty-pos)
  (db/set-cache! ::down? false)
  
  (events/register-event-handler!
    "mousemove"
    ;; Handler
    (fn [event]
      (let [type (if (db/get-cache ::down?) :mouse-drag :mouse-move)
            data (update-mouse-position! event false)]
        [type data]))
    ;; Merger
    (fn [older newer]
      (assoc newer :rel (merge-with + (:rel older) (:rel newer)))))

  (events/register-event-handler!
    "mousedown"
    ;; Handler
    (fn [event]
      (when-not (db/get-cache ::down?) ;; Ignore unexpected mousedowns — this was written defensively, not as a bugfix
        (let [data (update-mouse-position! event true)]
          (db/set-cache! ::down? true)
          [:mouse-down data])))
    ;; Merger
    (fn [older newer]
      newer))
      
  (events/register-event-handler!
    "mouseup"
    ;; Handler
    (fn [event]
      (when (db/get-cache ::down?) ;; Ignore unexpected mouseups — this was written defensively, not as a bugfix
        (let [data (update-mouse-position! event true)]
        (db/set-cache! ::down? false)
        [:mouse-up data])))
    ;; Merger
    (fn [older newer]
      newer)))
