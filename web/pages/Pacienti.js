import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref, onValue, get } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
const firebaseConfig = {
  // ...
  apiKey: "AIzaSyDHsp8a_NU7F8J-Gr5DaaWsoPUN79JO_ZY",

  authDomain: "proiectip-f2b57.firebaseapp.com",

  databaseURL: "https://proiectip-f2b57-default-rtdb.firebaseio.com",

  projectId: "proiectip-f2b57",

  storageBucket: "proiectip-f2b57.appspot.com",

  messagingSenderId: "134775428923",

  appId: "1:134775428923:web:a3542cd019c1e515c4891e",

  measurementId: "G-EGZ06QWFGX"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
// Get a reference to the database
const database = getDatabase(app);
const usersRef = ref(database, 'Users/');
// Retrieve data
const usersDiv = document.querySelector('#users');
onValue(usersRef, (snapshot) => {
  const users = snapshot.val();
  for (let userId in users) {
    const user = users[userId];
    const card = document.createElement('div');
    card.classList.add('card');
    card.innerHTML = `
      <img src="../images/person.png" style="max-width:40%;max-height:40%;object-fit:fill;">
      <p>${user.nume}</p>
      `;
    usersDiv.appendChild(card);
  }
});
