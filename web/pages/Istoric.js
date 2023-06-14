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
const uidParam = urlParams.get('uid');
const histRef = ref(database, `path/to/Solicitari/${uid}`);
const usersRef = ref(database, 'Users/');
console.log('uid = ' + uid);
const auth = getAuth();
onAuthStateChanged(auth, (user) => {
  const urlParams = new URLSearchParams(window.location.search);
  const uidParam = urlParams.get('uid');

  if (!user ) {
    //window.location.href = 'index.html'; // Redirect to index.html if no user is logged in or uid does not match
  } else {
    const userTypeRef = ref(database, `Users/${user.uid}/userType`);
    onValue(userTypeRef, (snapshot) => {
      const userType = snapshot.val();
      if (userType === 'medic' || userType === 'admin' || (uidParam && user.uid === uidParam)) {
        showAuthenticatedElements();
      } else {
        window.location.href = 'index.html'; // Redirect to index.html if user is not of type 'medic'
      }
    });
  }
});
let users;
let currentUser;
let userData;
let userType;
let currentUserType;
onValue(usersRef, (snapshot) => {
  users = snapshot.val();
  currentUser = auth.currentUser;
  userData = users[currentUser.uid];
  uid = currentUser.uid;
  console.log(currentUser.uid);
});

onValue(usersRef, (snapshot) => {
  snapshot.forEach((childSnapshot) => {
    if (childSnapshot.key === currentUser.uid) {
      currentUserType = childSnapshot.val().userType;
      console.log(currentUserType);
    }
  });
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
const histsDiv = document.querySelector('#cards');
onValue(histRef, (snapshot) => {
  snapshot.forEach((hist) => {
      const card = document.createElement('div');
      card.classList.add('card');
      card.innerHTML = `
      <h2>${hist.val().Motiv_solicitare}</h2>
      <p>${hist.val().Detalii_solicitare}</p>
      `;
      histsDiv.appendChild(card);
  });
});
const fisMed = document.getElementById('fisMed');
fisMed.addEventListener('click', () => {
    window.location.href = `FisaMedicala.html?uid=${uidParam}`;
});
if(userType === 'pacient'){
  document.getElementsByClassName('v5_47033').innerHTML = 'Recomandări';
}
else{
  document.getElementsByClassName('v5_47033').innerHTML = 'Pacienti';
  
}
const rec = document.getElementById('rec');
rec.addEventListener('click', () => {
  if(currentUserType === 'pacient'){
      window.location.href = `RecomandarePacient.html?uid=${uidParam}`;
  }
  else{
      window.location.href = `RecomandareMedic.html?uid=${uidParam}`;

  }    
});
const profil = document.getElementById('profil');
profil.addEventListener('click', () => {
    window.location.href = `Profil.html?uid=${currentUser.uid}`;
});