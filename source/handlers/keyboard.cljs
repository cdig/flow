;; Maps browser keyboard events into nice data

;; Side Effects √
;; State √
;; Deps √

(ns handlers.keyboard
  (:require [system.events :as events]
            [web.keyboard :as keyboard]))

(defonce pressing (atom #{}))

(defn setup! []
  
  (events/register-event-handler!
    "keydown"
    ;; Handler
    (fn [event]
      (let [changed-key (keyboard/event->keyword event)]
        (when-not (get @pressing changed-key) ;; Ignore key repeat
          [:key-down [changed-key (swap! pressing conj changed-key)]])))
    ;; Merger
    (fn [older newer]
      newer))

  (events/register-event-handler!
    "keyup"
    ;; Handler
    (fn [event]
      (let [changed-key (keyboard/event->keyword event)]
        [:key-up [changed-key (swap! pressing disj changed-key)]]))
    ;; Merger
    (fn [older newer]
      newer)))
