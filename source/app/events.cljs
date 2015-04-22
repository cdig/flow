;; Allows other systems to listen for and handle browser events, and keeps a log and set of recent events.
;; The log is used to determine which events happened, in order. The set is for quickly checking if an event occurred at all.
;; To improve perf, if two events of the same kind occur in succession, we will collapse them to a single value in the log.
;; This means handlers also need to provide a function that can be used to merge two instances of an event together.
;; The log/set is to be "drained" at periodic intervals, to make event application deterministic and performant.

(ns app.events
  (:require [app.db :as db]
            [browser.events :refer [addWindowListener!]]))

(defn- enqueue-event!
  "Receives a handler-processed event vector with a type keyword and a data map, and a merger function. If this event type matches the previously-logged event type, we need to merge these two event instances into a single value using the provided merger function. Also, if either the type or data returned from the handler are nil, we can safely ignore this event, as this is an explicit sign that the handler deemed this event unimportant."
  [[type data] merger]
  (when (and type data)
    (db/update-cache! ::set conj type)
    (let [log (db/get-cache ::log)
          [prev-type prev-data] (last log)]
      (db/set-cache! ::log
        (if (keyword-identical? prev-type type)
            (conj (pop log) [type (merger prev-data data)])
            (conj log [type data]))))))

(defn- clear!
  "Reset our internal event logs to empty values."
  []
  (db/set-cache! ::log [])
  (db/set-cache! ::set #{}))

;; PUBLIC

(defn drain-events!
  "Get all the events that have occurred since last call. Returns a vector: [log set]"
  []
  (let [events [(db/get-cache ::log) (db/get-cache ::set)]]
    (clear!)
    events))

(defn register-event-handler!
  "Register a handler (function) that will be called whenever a given event-name (string) occurs. Wraps addEventListener on window, so event-name strings should be given accordingly. Handler functionss should take the event data object, and return a vector with two elements: a (possibly different) event-name keyword, and a data map containing nicely-processed event data. The vector will be added to the event log for later use. If two of the same event are logged in succession, we will use the provided merger function to merge their values, so there's only ever 1 consecutive value for a given event type in the log."
  [event-name handler merger]
  (addWindowListener!
    event-name
    #(-> %
         handler
         (enqueue-event! merger))))

(defn setup!
  "Make sure we initialize the log/set to the right datatypes before we start using them."
  []
  (clear!))
