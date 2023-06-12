function showAuthenticatedElements() {
  const elements = document.querySelectorAll('.authenticated');
  elements.forEach((element) => {
    element.style.display = 'block';
  });
}

// Helper function to hide authenticated elements
function hideAuthenticatedElements() {
  const elements = document.querySelectorAll('.authenticated');
  elements.forEach((element) => {
    element.style.display = 'none';
  });
}
hideAuthenticatedElements();
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase, ref, onValue } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
import { getAuth, signOut, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

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

const urlParams = new URLSearchParams(window.location.search);
let uid = urlParams.get('uid');
const usersRef = ref(database, `path/to/Recomandari/${uid}`);

// Retrieve data
let userType = '';
var userData = '';
const auth = getAuth();
onAuthStateChanged(auth, (user) => {
  if (user) {
    showAuthenticatedElements();
    uid = user.uid;
  } else {
    window.location.href = 'index.html'; // Redirect to index.html if no user is logged in
  }
});
onValue(usersRef, (snapshot) => {
  const users = snapshot.val();
  const currentUser = auth.currentUser;
  if (!users) {
    usersDiv.innerHTML = '<h1>Nu exista recomandari</h1>'
  }
  
  // Display the user's data on the web page
});
var logout = document.querySelector('#logout');
logout.addEventListener('click', (e) => {
  e.preventDefault();
  console.log('in eventlistener');
  signOut(auth).then(() => {
    alert('Signed out successfully');
    document.location = 'index.html';
  }).catch((error) => {
    console.log('Not logged in');
  });
});

const usersDiv = document.querySelector('#users');
//if (usersDiv.innerHTML !== '') {
  onValue(usersRef, (snapshot) => {
    snapshot.forEach((recomandare) => {
      const card = document.createElement('div');
      card.classList.add('card');
      card.innerHTML = `
      <h1>${recomandare.val().text}</h1>
      <p>${recomandare.val().date}</p>
      `;
      usersDiv.appendChild(card);
    });
  });
//}
const fisMed = document.getElementById('fisMed');
fisMed.addEventListener('click', () => {
  window.location.href = `FisaMedicala.html?uid=${uid}`;
});
const profil = document.getElementById('profil');
profil.addEventListener('click', () => {
  window.location.href = `Profil.html?uid=${uid}`;
});
const hist = document.getElementById('hist');
hist.addEventListener('click', () => {
  window.location.href = `Istoric.html?uid=${uid}`;
});