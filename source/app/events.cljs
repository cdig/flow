;; Allows other systems to separately listen for browser events, process the event data, and act accordingly.
;; When events occur, we store the processed data in a temporary log and set.
;; The log/set is to be "drained" at periodic intervals, so that the events may be acted upon.
;; This "chunking" approach makes event-handling deterministic and replay-friendly.
;; The event-log is used to determine which events happened, in order.
;; The event-set is to quickly check if a given event occurred at all.
;; To improve perf, if two events of the same kind occur in succession, we will collapse them to a single value in the log.
;; This means handlers also need to provide a function that can be used to merge two instances of an event together.

(ns app.events
  (:require [browser.events :refer [addWindowListener!]]))

(defonce event-log (atom []))
(defonce event-set (atom #{}))

(defn- enqueue-event!
  "Receives a handler-processed event vector with a type keyword and a data map, and a merger function. If this event type matches the previously-logged event type, we need to merge these two event instances into a single value using the provided merger function. Also, if either the type or data returned from the handler are nil, we can safely ignore this event, as this is an explicit sign that the handler deemed this event unimportant."
  [[type data] merger]
  (when (and type data)
    (swap! event-set conj type)
    (let [log @event-log
          [prev-type prev-data] (last log)]
      (reset! event-log
        (if (= prev-type type)
            (conj (pop log) [type (merger prev-data data)])
            (conj log [type data]))))))

(defn- clear!
  "Reset our log and set to empty values."
  []
  (reset! event-log [])
  (reset! event-set #{}))

;; PUBLIC

(defn drain!
  "Get all the events that have occurred since last call. Takes a map assoces the log and set under the keys :event-log and :event-set."
  [m]
  (let [new-map (assoc m :event-log @event-log :event-set @event-set)]
    (clear!)
    new-map))

(defn register-event-handler!
  "Register a handler (function) that will be called whenever a given event-name (string) occurs. Wraps addEventListener on window, so event-name strings should be given accordingly. Handler functionss should take the event data object, and return a vector with two elements: a (possibly different) event-name keyword, and a data map containing nicely-processed event data. The vector will be added to the event log for later use. If two of the same event are logged in succession, we will use the provided merger function to merge their values, so there's only ever 1 consecutive value for a given event type in the log."
  [event-name handler merger]
  (addWindowListener!
    event-name
    (fn [e]
      ; (.preventDefault e) ;; Causes the mouse to flicker
      (.stopPropagation e)
      (enqueue-event! (handler e) merger))))
