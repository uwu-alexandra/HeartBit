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
const usersRef = ref(database, 'Users/');

const auth = getAuth();
let uid;
onAuthStateChanged(auth, (user) => {
  const urlParams = new URLSearchParams(window.location.search);
  const uidParam = urlParams.get('uid');

  if (!user || (uidParam && user.uid !== uidParam)) {
    window.location.href = 'index.html'; // Redirect to index.html if no user is logged in or uid does not match
  } else {
    const userTypeRef = ref(database, `Users/${user.uid}/userType`);
    onValue(userTypeRef, (snapshot) => {
      const userType = snapshot.val();
      if (userType === 'medic' || (uidParam && user.uid === uidParam)) {
        showAuthenticatedElements();
      } else {
        window.location.href = 'index.html'; // Redirect to index.html if user is not of type 'medic'
      }
    });
  }
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

// Retrieve data
const usersDiv = document.querySelector('#users');
onValue(usersRef, (snapshot) => {

  snapshot.forEach((user) => {
    if (user.val().userType === 'pacient') {
      const card = document.createElement('div');
      card.classList.add('card');
      card.innerHTML = `
      <img src="../images/person.png" style="max-width:40%;max-height:40%;object-fit:fill;">
      <p>${user.val().nume} ${user.val().prenume}</p>
      `;
      card.addEventListener('click', () => {
        window.location.href = `FisaMedicala.html?uid=${user.key}`;
      });
      usersDiv.appendChild(card);
    }
  });
});

const searchBar = document.getElementById('searchBar');
searchBar.addEventListener('input', () => {
  const query = searchBar.value.toLowerCase();
  usersDiv.innerHTML = ''; // Clear previous results
  onValue(usersRef, (snapshot) => {
    const users = snapshot.val();
    snapshot.forEach((user) => {
      if (((user.val().nume.toLowerCase().includes(query) || user.val().prenume.toLowerCase().includes(query)) || (user.val().nume.toLowerCase() + ' ' + user.val().prenume.toLowerCase()).includes(query)) && user.val().userType === 'pacient') {
        {
          const card = document.createElement('div');
          card.classList.add('card');
          card.innerHTML = `
          <img src="../images/person.png" style="max-width:40%;max-height:40%;object-fit:fill;">
          <p>${user.val().nume} ${user.val().prenume}</p>
          `;
          card.addEventListener('click', () => {
            window.location.href = `FisaMedicala.html?uid=${user.key}`;
          });
          usersDiv.appendChild(card);
        }
      }
    });
  });
});

const profil = document.getElementById('profil');
profil.addEventListener('click', () => {
  window.location.href = `Profil.html?uid=${uid}`;
});