;; Stores all the data used in our app.
;; Data is organized into two groups.
;; The "state" group is for data that should be persisted and restored.
;; The "cache" group is for volatile data that can be regenerated on-demand.

(ns app.db)

;; This is data that should be persisted
(def initial-state {})
(defonce state (atom initial-state))

;; This is data that can be generated on the fly
(def initial-cache {})
(defonce cache (atom initial-cache))

;; FUNCTIONS

(defn clear!
  "Resets the DB state and cache to their initial values"
  []
  (reset! state initial-state)
  (reset! cache initial-cache))


(defn get-state
  "Retreive a saved value. The first argument should be a namespaced keyword."
  [k]
  (get @state k))

(defn set-state!
  "Assign a saved value. The first argument should be a namespaced keyword. The second arg should be the new value."
  [k v]
  (swap! state assoc k v)
  v)

(defn update-state!
  "Update a saved value. The first argument should be a namespaced keyword. The second, a function that will be called with the current value for that keyword and any additional args."
  [k f & args]
  (let [v (apply f (get-state k) args)]
    (set-state! k v)
    v))


(defn get-cache
  "Retreive a cached value. The first argument should be a namespaced keyword."
  [k]
  (get @cache k))

(defn set-cache!
  "Assign a cached value. The first argument should be a namespaced keyword. The second arg should be the new value."
  [k v]
  (swap! cache assoc k v)
  v)

(defn update-cache!
  "Update a cached value. The first argument should be a namespaced keyword. The second, a function that will be called with the current value for that keyword and any additional args."
  [k f & args]
  ; (apply swap! cache update k f args))
  (let [v (apply f (get-cache k) args)]
    (set-cache! k v)
    v))
