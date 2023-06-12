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
// Import the functions you need from the SDKs you need

//import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";
import { getDatabase,onValue, ref } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js";
import { getAuth, signInWithEmailAndPassword, onAuthStateChanged, setPersistence, browserSessionPersistence, signOut } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";


// TODO: Add SDKs for Firebase products that you want to use

// https://firebase.google.com/docs/web/setup#available-libraries


// Import the functions you need from the SDKs you need

import { initializeApp } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js";

// TODO: Add SDKs for Firebase products that you want to use

// https://firebase.google.com/docs/web/setup#available-libraries


// Your web app's Firebase configuration
const firebaseConfig = {

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

const app = initializeApp(firebaseConfig);/*
const userData = localStorage.getItem('currentUser');
const user = JSON.parse(userData);*/
const auth = getAuth();

//console.log(user);
onAuthStateChanged(auth, (user) => {
  const urlParams = new URLSearchParams(window.location.search);
  const uidParam = urlParams.get('uid');

  if (!user || (uidParam && user.uid !== uidParam)) {
    window.location.href = 'index.html'; // Redirect to index.html if no user is logged in or uid does not match
  } else {
    const userTypeRef = ref(database, `Users/${user.uid}/userType`);
    onValue(userTypeRef, (snapshot) => {
      const userType = snapshot.val();
      if ((userType === 'medic' || userType === 'admin') && (uidParam && user.uid === uidParam)) {
        showAuthenticatedElements();
      } else {
        window.location.href = 'index.html'; // Redirect to index.html if user is not of type 'medic'
      }
    });
  }
});
var logout = document.querySelector('#logout');
const urlParams = new URLSearchParams(window.location.search);
const uid = urlParams.get('uid');
const database = getDatabase(app);
const usersRef = ref(database, 'Users');
onValue(usersRef, (snapshot) => {
  const users = snapshot.val();
  const currentUser = auth.currentUser;
  const userData = users[currentUser.uid];
  console.log(currentUser.uid);
  // Display the user's data on the web page
  const userDiv = document.getElementById('v5_47020');
  userDiv.innerHTML = `Welcome, ${userData.nume} ${userData.prenume}`;
});
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
const profil = document.getElementById('profil');
profil.addEventListener('click', () => {
    window.location.href = `Profil.html?uid=${uid}`;
});








