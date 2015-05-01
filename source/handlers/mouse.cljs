;; Maps browser mouse events into nice data

;; Side Effects √
;; State √
;; Deps √

(ns handlers.mouse
  (:require [app.events :as events]
            [browser.mouse :as mouse]))

(def empty-pos {:x 0 :y 0})
(defonce a-abs (atom empty-pos))
(defonce a-delta (atom empty-pos))
(defonce a-down? (atom false))

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
        rel (merge-with - abs @a-abs)
        delta (if reset-delta?
                  empty-pos
                  (merge-with + rel @a-delta))]
    (when (or reset-delta? (has-moved? rel))
      (reset! a-abs abs)
      (reset! a-delta delta)
      {:abs abs
       :rel rel
       :delta delta})))

;; PUBLIC

(defn setup! []
  
  (events/register-event-handler!
    "mousemove"
    ;; Handler
    (fn [event]
      (let [type (if @a-down? :mouse-drag :mouse-move)
            data (update-mouse-position! event false)]
        (when data [type data])))) ;; Return nil if there's no data

  (events/register-event-handler!
    "mousedown"
    ;; Handler
    (fn [event]
      (when-not @a-down? ;; Ignore unexpected mousedowns — this was written defensively, not as a bugfix
        (let [data (update-mouse-position! event true)]
          (reset! a-down? true)
          [:mouse-down data]))))
      
  (events/register-event-handler!
    "mouseup"
    ;; Handler
    (fn [event]
      (when @a-down? ;; Ignore unexpected mouseups — this was written defensively, not as a bugfix
        (let [data (update-mouse-position! event true)]
        (reset! a-down? false)
        [:mouse-up data])))))
