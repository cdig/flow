;; Maps browser rAF into tick broadcasts, and turns the rAF time into dT

;; Side Effects √
;; State √
;; Deps √

(ns handlers.tick
  (:require [app.messenger :as messenger]
            [browser.window :as window]))

(defonce lastTime (atom nil))

(defn tick [time-ms]
  (let [time (/ time-ms 1000)]
    (messenger/broadcast! :tick (- time (or @lastTime time)))
    (reset! lastTime time)
    (window/rAF! tick)))

(defn initialize! []
  
  (window/rAF! tick))

(defonce initialized (do (initialize!) true))
