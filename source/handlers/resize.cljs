;; Maps browser window resize events into nice data

;; Side Effects √
;; State X
;; Deps √

(ns handlers.resize
  (:require [app.messenger :as messenger]
            [browser.window :as window]))

(defn initialize! []

  (messenger/register-event-handler! :resize
    (fn [& _]
      {:w (window/width)
       :h (window/height)})))

(defonce initialized (do (initialize!) true))
