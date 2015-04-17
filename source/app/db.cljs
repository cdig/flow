(ns app.db)

;; This is data that should be persisted
(defonce state (atom {
  
  ;; Entities active in the system
  ;; eid -> entity state (see default entity for structure)
  :entities {}
  
  ;; Behaviours actively belonging to entities
  ;; bid -> behaviour
  :behaviours {}
  
  }))

;; This is data that can be generated on the fly
(defonce cache (atom {
  
  ;; Temporary: to get next anonymous entity ID
  ;; Should use UUIDs
  :next-eid 0
  
  ;; Temporary: to get next anonymous behaviour ID
  ;; Should use UUIDs
  :next-bid 0
  
  ;; Behaviour types available for creation
  ;; bname -> behaviour template
  :behaviour-templates {}
  
  ;; Default entity structure
  ;; This is stored as data so that we can change it at runtime for debugging, etc
  ;; All entities are created based on this structure
  :default-entity {:eid nil
                   :bids #{}}
  
  ;; Default behaviour structure
  ;; This is stored as data so that we can change it at runtime for debugging, etc
  ;; Behaviours merge this structure + template + custom values supplied at creation
  :default-behaviour {:bid nil
                      :bname nil
                      :state {}}
  
  ;; Indexes for fast lookup
  ; :message->bname->callback {}
  :message->sname->callback {}
  :bname->snames {} ;; snames is a set
  :sname->bids {} ;; bids is a set
  :bid->eid {}
  }))

;; Called with either the literal :eid or :bid
;; Returns a new id of the form :eid-123 or :bid-123
;; Temporary — should use UUIDs
(defn next-id! [type]
  (let [type-name (name type)
        cache-key (keyword (str "next-" type-name))
        id (keyword (str type-name "-" (@cache cache-key)))]
    (swap! cache update cache-key inc)
    id))
