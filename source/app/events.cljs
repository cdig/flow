;; Allows other systems to listen for and handle browser events, and keeps a queue of their results.
;; The queue can be drained at periodic intervals, to make event application deterministic and performant.

(ns app.events
  (:require [app.db :as db]
            [browser.events :refer [addEventListener!]]))

(defn- enqueue! [event]
  (when event
    (db/update-cache! ::queue conj event)))

;; PUBLIC

(defn drain-queue!
  "Empties the queue, and returns whatever events were in it before it was emptied."
  []
  (let [events (db/get-cache ::queue)]
    (db/set-cache! ::queue [])
    events))

(defn register-event-handler!
  "Register a handler (function) that will be called whenever a given event-name (string) occurs. Wraps addEventListener on window, so event-name strings should be given accordingly. Handlers should take the event data object, and return a vector with a (possibly different) event-name keyword, and a data map containing nicely processed event data. The vector will be added to the event queue for later use."
  [event-name handler]
  (addEventListener!
    event-name
    (comp enqueue! handler)))

(defn initialize! []
  (db/set-cache! ::queue []))
(defonce initialized (do (initialize!) true))
