;; Wraps the browser Math object and APIs

;; Side Effects X
;; State X
;; Deps —

(ns web.math)

(def Math js/Math)

;; CONSTANTS

(def PI Math.PI)

;; FUNCTIONS

(defn sin [i] (Math.sin i))
(defn cos [i] (Math.cos i))

(defn sqrt [i] (Math.sqrt i))
(defn round [i] (Math.round i))

(defn random [] (Math.random))
