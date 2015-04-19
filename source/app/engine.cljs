;; The engine manages the run loop for our app.
;; In the current implementation, it wraps the browser raf.
;; It takes a callback function at the start, which it calls with a dT (seconds) on each tick.

(ns app.engine
  (:require [app.db :as db]
            [browser.window :as window]))

(defn- tick
  "It's time to update the system. Calculate our dT since the last tick, and call the callback."
  [time-ms]
  (let [time (/ time-ms 1000)
        dT (- time (db/get-cache ::time))]
    ((db/get-cache ::callback) dT)
    (db/set-cache! ::time time)
    (window/rAF! tick)))

(defn- first-tick!
  "The first tick after we start the engine is just a warmup — we don't call the callback. This keeps the first dT from being eratic."
  [time-ms]
  (db/set-cache! ::time (/ time-ms 1000))
  (window/rAF! tick))

;; PUBLIC

(defn start!
  "Start running the engine. Call the given callback on each tick. The callback should accept a single argument: the dT (delta time) in seconds since the last tick."
  [callback]
  (db/set-cache! ::callback callback)
  (when-not (db/get-cache ::running)
    (db/set-cache! ::running true)
    (window/rAF! first-tick!)))
