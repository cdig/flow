;; Maps browser mouse events into nice data

;; Side Effects √
;; State X
;; Deps √

(ns handlers.mouse
  (:require [app.messenger :as messenger]
            [browser.mouse :as mouse]))

(defn initialize! []

  (messenger/register-event-handler! :mousemove
    mouse/event->pos)

  (messenger/register-event-handler! :mousedown
    (fn [& _] {:down true}))

  (messenger/register-event-handler! :mouseup
    (fn [& _] {:down false})))

(defonce initialized (do (initialize!) true))
