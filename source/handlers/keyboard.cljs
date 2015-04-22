;; Maps browser keyboard events into nice data

;; Side Effects √
;; State √
;; Deps √

(ns handlers.keyboard
  (:require [app.db :as db]
            [app.events :as events]
            [browser.keyboard :as keyboard]))

(defn setup! []
  
  (db/set-cache! ::pressing #{})
  
  (events/register-event-handler!
    "keydown"
    ;; Handler
    (fn [event]
      (let [changed-key (keyboard/event->keyword event)
            pressing (db/update-cache! ::pressing conj changed-key)]
        [:key-down [changed-key pressing]]))
    ;; Merger
    (fn [older newer]
      newer))

  (events/register-event-handler!
    "keyup"
    ;; Handler
    (fn [event]
      (let [changed-key (keyboard/event->keyword event)
            pressing (db/update-cache! ::pressing disj changed-key)]
        [:key-up [changed-key pressing]]))
    ;; Merger
    (fn [older newer]
      newer)))
