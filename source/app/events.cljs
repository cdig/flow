;; Allows other systems to listen for and handle browser events, and keeps a queue of their results.
;; The queue can be drained at periodic intervals, to make event application deterministic and performant.
;; We use a map for the queue, which lets us quickly determine whether a particular kind of event has occurred.
;; Also, this means we can only have one instance of an event in the queue at once, which is desirable for perf.
;; That means handlers also need to provide a function that can be used to merge two instances of an event together.
;; This merging behaviour could cause bugs if a mouse move happens, then a mouse down, then another move, all in one queue.

(ns app.events
  (:require [app.db :as db]
            [browser.events :refer [addWindowListener!]]))

(defn- enqueue!
  [[type data] merger]
  (when type ;; a handler may return nil if it wishes to discard the changes
    (let [queue (db/get-cache ::queue)
          old-data (get queue type)]
      (db/update-cache! ::queue assoc type
        (if old-data ;; we can't use merge-with here, unfortunately
            (merger old-data data)
            data)))))

(defn- clear-queue!
  []
  (db/set-cache! ::queue {}))

;; PUBLIC

(defn drain-queue!
  "Empties the queue, and returns whatever events were in it before it was emptied."
  []
  (let [events (db/get-cache ::queue)]
    (clear-queue!)
    events))

(defn register-event-handler!
  "Register a handler (function) that will be called whenever a given event-name (string) occurs. Wraps addEventListener on window, so event-name strings should be given accordingly. Handlers should take the event data object, and return a vector with a (possibly different) event-name keyword, and a data map containing nicely processed event data. The vector will be mangled and merged into the event queue for later use. If two of the same event fire in quick succession, we will use the provided merger function to merge their values, so there's only ever 1 value for an event in the queue."
  [event-name handler merger]
  (addWindowListener!
    event-name
    #(-> %
         handler
         (enqueue! merger))))

(defn setup!
  "Make sure we initialize the queue to the right datatype before we start using it."
  []
  (clear-queue!))
