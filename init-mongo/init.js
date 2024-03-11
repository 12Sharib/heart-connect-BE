// init.js

db.createUser({
  user: "admin",
  pwd: "admin",
  roles: [{ role: "readWrite", db: "heart-connect" }]
});
