;; Coordinates message broadcast, handling, transmission, and receipt

;; A message is just a keyword representing a system event,
;; and optionally n pieces of event data

;; A handler is a function that manipulates the data before we forward it on
;; Each message type may have 0 or 1 handlers
;; A handler must take n arguments and return a collection

;; A transmitter is a function that will be called whenever ANY message happens
;; It can log the message + data, or forward it to other systems, etc
;; The transmitter must take 2 arguments
;; — the message type
;; — the collection of message data post-handling

;; A receiver is a function to be called whenever a certain message happens
;; It must take n args

;; Side Effects √
;; State √
;; Deps √

(ns app.messenger
  (:require [browser.events :as events]))

(defonce transmitters (atom []))
(defonce receivers (atom {}))
(defonce handlers (atom {}))
(defonce message-queue (atom []))

(defn identity-handler [& inputs] inputs)

(defn- broadcast-all! [queue]
  (doseq [[type input] queue]
    (let [handler (get @handlers type identity-handler)
          output (flatten (list (apply handler input)))]
      (doseq [transmitter @transmitters]
        (transmitter type output))
      (doseq [receiver (@receivers type)]
        (apply receiver output)))))

(defn broadcast! [type & input]
  (swap! message-queue conj [type input])
  (when (= type :tick)
    (broadcast-all! @message-queue)
    (reset! message-queue [])))
  
(defn register-transmitter! [transmitter]
  (reset! transmitters (conj @transmitters transmitter)))

(defn register-receiver! [type receiver]
  (reset! receivers (update @receivers type conj receiver)))

(defn register-handler! [type handler]
  (reset! handlers (assoc @handlers type handler)))

(defn register-event-handler! [type handler]
  (when-not (type @handlers)
    (events/addEventListener! (name type) (partial broadcast! type)))
  (register-handler! type handler))
