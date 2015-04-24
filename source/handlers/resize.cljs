;; Maps browser resize events into nice data

;; Side Effects √
;; State X
;; Deps √

(ns handlers.resize
  (:require [app.events :as events]
            [browser.window :as window]))

(defn setup! []
  (events/register-event-handler!
    "resize"
    ;; Handler
    (fn [event]
      [:resize {:w (window/width)
                :h (window/height)}])
    ;; Merger
    (fn [older newer]
      newer)))
      
