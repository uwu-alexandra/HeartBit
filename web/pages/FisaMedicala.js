
function showAuthenticatedElements() {
  const elements = document.querySelectorAll('.authenticated');
  elements.forEach((element) => {
    element.style.display = 'block';
    document.getElementById('form').style.display = 'none';
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
import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.19.1/firebase-app.js';
import { getDatabase, ref, onValue, limitToLast, set, query, get } from 'https://www.gstatic.com/firebasejs/9.19.1/firebase-database.js';
import { getAuth, onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/9.19.1/firebase-auth.js";

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

initializeApp(firebaseConfig);
const database = getDatabase();


const urlParams = new URLSearchParams(window.location.search);
const key = urlParams.get('uid');
const ekgRef = ref(database, `/path/to/Senzori/${key}/EKG/`);
const temperatureRef = ref(database, `/path/to/Senzori/${key}/TEMP/`);
const pulseRef = ref(database, `/path/to/Senzori/${key}/PULS/`);
const umdRef = ref(database, `/path/to/Senzori/${key}/UMD/`);
const temperatureTable = document.getElementById('temperatureTable');

const auth = getAuth();


let currentUserType;
let userType;
const usrRef = ref(database, 'Users');
let currentUser = '';
let userData = '';
let users = ''; let uid = '';
onAuthStateChanged(auth, (user) => {
  const urlParams = new URLSearchParams(window.location.search);
  const uidParam = urlParams.get('uid');

  if (!user && (uidParam && user.uid !== uidParam)) {
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
onValue(usrRef, (snapshot) => {
  users = snapshot.val();
  currentUser = auth.currentUser;
  if (currentUser) {
    userData = users[currentUser.uid];
    uid = currentUser.uid;
    console.log(currentUser.uid);
  }
  else
    document.location('index.html');
});

onValue(usrRef, (snapshot) => {
  snapshot.forEach((childSnapshot) => {
    if (childSnapshot.key === uid) {
      currentUserType = childSnapshot.val().userType;
      console.log(currentUserType);
    }
  });
});
let nume = '';
var ekg_low = 0;
var ekg_high = 0;

var puls_low = 0;
var puls_high = 0;

var temp_low = 0;
var temp_high = 0;

var umid_low = 0;
var umid_high = 0;
console.log(currentUserType);
const user = document.getElementById('user');
onValue(usrRef, (snapshot) => {
  snapshot.forEach((childSnapshot) => {
    if (childSnapshot.key === key) {
      ekg_low = childSnapshot.val().ekg_low;
      ekg_high = childSnapshot.val().ekg_high;

      puls_low = childSnapshot.val().puls_low;
      puls_high = childSnapshot.val().puls_high;

      temp_low = childSnapshot.val().temp_low;
      temp_high = childSnapshot.val().temp_high;

      umid_low = childSnapshot.val().umid_low;
      umid_high = childSnapshot.val().umid_high;
      userType = childSnapshot.val().userType;
      nume = `${childSnapshot.val().nume} ${childSnapshot.val().prenume}`;
      let gen = 'x';
      if (childSnapshot.val().cnp.startsWith('1') || childSnapshot.val().cnp.startsWith('5'))
        gen = 'M';
      else
        gen = 'F';
      let an = '';
      if (childSnapshot.val().cnp.startsWith('1') || childSnapshot.val().cnp.startsWith('2'))
        an = '19' + childSnapshot.val().cnp.substring(1, 3);
      else
        an = '20' + childSnapshot.val().cnp.substring(1, 3);
      user.innerHTML = `
          <div class="container">
          <h1 class="info-heading">Nume: ${childSnapshot.val().nume} ${childSnapshot.val().prenume}</h1>
          <h1 class="info-heading">Email: ${childSnapshot.val().email}</h1>
          <h1 class="info-heading">CNP: ${childSnapshot.val().cnp}</h1>
          <h1 class="info-heading">ID: ${childSnapshot.val().id}</h1>
          <h1 class="info-heading">Data nasterii: ${an}/${childSnapshot.val().cnp.substring(3, 5)}/${childSnapshot.val().cnp.substring(5, 7)}</h1>
          <h1 class="info-heading">Gen: ${gen}</h1>
        </div>
          `
    }
  });
});
let lastNEntriesQuery = query(ekgRef, limitToLast(50));

onValue(lastNEntriesQuery, (snapshot) => {
  const pulseTable = document.getElementById('pulseTable');
  pulseTable.innerHTML = '';
  pulseTable.innerHTML = '<h2>Puls</h2><table class="table"><tr><th>Data</th><th>Puls</th><th>Status</th></tr></table>';
  const pulseTableBody = pulseTable.querySelector('table');
  const pulseData = [];
  snapshot.forEach((childSnapshot) => {
    const pulse = Number(childSnapshot.val().Valoare);
    const timestamp = childSnapshot.val().Time_stamp;
    const status = (pulse >= ekg_low && pulse <= ekg_high) ? 'DA' : 'NU';
    const row = `<tr><td>${timestamp}</td><td>${pulse}</td><td>${status}</td></tr>`;
    pulseTableBody.innerHTML += row;

    pulseData.push({ timestamp, pulse });
  });


  pulseData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

  const last50Entries = pulseData.slice(-50); // Get the last 50 entries

  google.charts.load('current', { packages: ['corechart'] });
  google.charts.setOnLoadCallback(() => generateLineGraph1(last50Entries));
});

function generateLineGraph1(data) {
  const dataTable = new google.visualization.DataTable();
  dataTable.addColumn('string', 'Data');
  dataTable.addColumn('number', 'EKG');

  data.forEach((item) => {
    dataTable.addRow([item.timestamp, item.pulse]);
  });

  const options = {
    title: 'EKG',
    curveType: 'function',
    legend: { position: 'bottom' },
    hAxis: { title: 'Data' },
    vAxis: { title: 'EKG' },
    width: 1000,
    height: 500,
  };

  const chart = new google.visualization.LineChart(document.getElementById('ekgChart'));
  chart.draw(dataTable, options);

}
const tempData = [];
try {

  const lastNEntriesQuery = query(temperatureRef, limitToLast(50));

  onValue(lastNEntriesQuery, (snapshot) => {
    temperatureTable.innerHTML = '';
    temperatureTable.innerHTML = '<h2>Temperatura</h2><table class="table"><tr><th>Data</th><th>Temperatura</th><th>Status</th></tr></table>';
    const temperatureTableBody = temperatureTable.querySelector('table');
    snapshot.forEach((childSnapshot) => {
      const temperature = childSnapshot.val().Valoare;
      const timestamp = childSnapshot.val().Time_stamp;
      const status = (temperature >= temp_low && temperature <= temp_high) ? 'DA' : 'NU';
      const row = `<tr><td>${timestamp}</td><td>${temperature}</td><td>${status}</td></tr>`;
      temperatureTableBody.innerHTML += row;
      tempData.push({ timestamp, temperature });
    });
    tempData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

    const last50Entries = tempData.slice(-50); // Get the last 50 entries

    google.charts.load('current', { packages: ['corechart'] });
    google.charts.setOnLoadCallback(() => generateLineGraph2(last50Entries));
  });
  function generateLineGraph2(data) {
    const dataTable = new google.visualization.DataTable();
    dataTable.addColumn('string', 'Data');
    dataTable.addColumn('number', 'Temperatura');

    data.forEach((item) => {
      dataTable.addRow([item.timestamp, item.temperature]);
    });

    const options = {
      title: 'Temperatura',
      curveType: 'function',
      legend: { position: 'bottom' },
      hAxis: { title: 'Data' },
      vAxis: { title: 'Temperatura' },
      width: 1000,
      height: 500,
    };

    const chart = new google.visualization.LineChart(document.getElementById('tempChart'));
    chart.draw(dataTable, options);
  }

}
catch { console.log('error'); }

lastNEntriesQuery = query(umdRef, limitToLast(50));

onValue(lastNEntriesQuery, (snapshot) => {

  const pulseData = [];
  snapshot.forEach((childSnapshot) => {
    const pulse = Number(childSnapshot.val().Valoare);
    const timestamp = childSnapshot.val().Time_stamp;
    pulseData.push({ timestamp, pulse });
  });


  pulseData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

  const last50Entries = pulseData.slice(-50); // Get the last 50 entries

  google.charts.load('current', { packages: ['corechart'] });
  google.charts.setOnLoadCallback(() => generateLineGraph3(last50Entries));
});

function generateLineGraph3(data) {
  const dataTable = new google.visualization.DataTable();
  dataTable.addColumn('string', 'Data');
  dataTable.addColumn('number', 'Umiditate');

  data.forEach((item) => {
    dataTable.addRow([item.timestamp, item.pulse]);
  });

  const options = {
    title: 'Umiditate',
    curveType: 'function',
    legend: { position: 'bottom' },
    hAxis: { title: 'Data' },
    vAxis: { title: 'Umiditate' },
    width: 1000,
    height: 500,

  };

  const chart = new google.visualization.LineChart(document.getElementById('humidChart'));
  chart.draw(dataTable, options);
}
lastNEntriesQuery = query(pulseRef, limitToLast(50));

onValue(lastNEntriesQuery, (snapshot) => {

  const pulseData = [];
  snapshot.forEach((childSnapshot) => {
    const pulse = Number(childSnapshot.val().Valoare);
    const timestamp = childSnapshot.val().Time_stamp;
    pulseData.push({ timestamp, pulse });
  });


  pulseData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

  const last50Entries = pulseData.slice(-50); // Get the last 50 entries

  google.charts.load('current', { packages: ['corechart'] });
  google.charts.setOnLoadCallback(() => generateLineGraph4(last50Entries));
});

function generateLineGraph4(data) {
  const dataTable = new google.visualization.DataTable();
  dataTable.addColumn('string', 'Data');
  dataTable.addColumn('number', 'Puls');

  data.forEach((item) => {
    dataTable.addRow([item.timestamp, item.pulse]);
  });

  const options = {
    title: 'Puls',
    curveType: 'function',
    legend: { position: 'bottom' },
    hAxis: { title: 'Data' },
    vAxis: { title: 'Puls' },
    width: 1000,
    height: 500,
  };

  const chart = new google.visualization.LineChart(document.getElementById('pulseChart'));
  chart.draw(dataTable, options);
}

var logout = document.querySelector('#logout');
logout.addEventListener('click', (e) => {
  e.preventDefault();
  console.log('in eventlistener');
  signOut(auth).then(() => {
    alert('Signed out successfully');
    document.location = 'index.html';
  }).catch(() => {
    console.log('Not logged in');
  });
});
const profil = document.getElementById('profil');
profil.addEventListener('click', () => {
  window.location.href = `Profil.html?uid=${key}`;
});
const rec = document.getElementById('rec');
rec.addEventListener('click', () => {
  if (currentUserType === 'medic')
    window.location.href = `RecomandareMedic.html?uid=${key}`;
  else
    window.location.href = `RecomandarePacient.html?uid=${key}`;
});
const hist = document.getElementById('hist');
hist.addEventListener('click', () => {
  window.location.href = `Istoric.html?uid=${key}`;
});
if (currentUserType === 'pacient') {
  document.getElementById('v5_47030').innerHTML = 'Profil';
}
else {
  document.getElementById('v5_47030').innerHTML = 'Pacienti';
}
profil.addEventListener('click', () => {
  if (currentUserType === 'pacient') {
    window.location.href = `Profil.html?uid=${uid}`;
  }
  else {
    window.location.href = `Pacienti.html`;
  }
});
rec.addEventListener('click', () => {
  if (currentUserType === 'pacient') {
    window.location.href = `RecomandarePacient.html?uid=${key}`;
  }
  else {
    window.location.href = `RecomandareMedic.html?uid=${key}`;

  }
});

window.addEventListener('DOMContentLoaded', () => {
  // Initialize Firebase app
  const app = initializeApp(firebaseConfig);

  // Get a reference to the database
  const database = getDatabase();

  const saveDataAsPDF = () => {
    console.log('uid = ' + key);
    const dataPath = `path/to/Senzori/${key}`;

    const dataRef = ref(database, dataPath);
    onValue(dataRef, (snapshot) => {
      const sensorData = snapshot.val();
      const { jsPDF } = window.jspdf;

      const doc = new jsPDF();

      doc.text(nume, 10, 10);

      const tableData = [];
      for (const entryKey in sensorData) {
        if (sensorData.hasOwnProperty(entryKey)) {
          const entry = sensorData[entryKey];
          for (const objKey in entry) {
            if (entry.hasOwnProperty(objKey)) {
              const obj = entry[objKey];
              tableData.push(obj);
            }
          }
        }
      }

      const tableHeaders = ['Alerta', 'Denumire', 'Identificator', 'Time_Stamp', 'Valoare'];

      doc.autoTable({
        head: [tableHeaders],
        body: tableData.map(obj => Object.values(obj)),
        startY: 20,
        theme: 'grid',
      });

      // Save the PDF document
      doc.save('data.pdf');
    }, (error) => {
      console.log('Error retrieving data:', error);
    });
  };

  // Add event listener to the download as JSON button
  const downloadJSONButton = document.getElementById('download');
  downloadJSONButton.addEventListener('click', saveDataAsPDF);
});
const edit = document.getElementById('edit');

const uRef = ref(database, `Users/${key}`);
edit.addEventListener('click', () => {
  const form = document.getElementById('form');
  if (form.style.display === 'none') {
    // Form is currently hidden, show the form
    form.style.display = 'block';

    // Get a reference to the user's node
    

    // Fetch the user's data from Firebase
    onValue(uRef, (snapshot) => {
      const userData = snapshot.val();

      // Generate the form fields
      const fields = ['ekg_low', 'ekg_high', 'puls_low', 'puls_high', 'temp_low', 'temp_high', 'umid_low', 'umid_high'];

      for (const field of fields) {
        const label = document.createElement('label');
        label.textContent = field;
        form.appendChild(label);

        const input = document.createElement('input');
        input.setAttribute('type', 'number');
        input.setAttribute('value', userData[field]);
        input.setAttribute('name', field);
        input.setAttribute('id', field);
        form.appendChild(input);

      }
      const saveButton = document.createElement('button');
      saveButton.setAttribute('type', 'submit');
      saveButton.setAttribute('id', 'save');
      saveButton.textContent = 'Save';
      form.appendChild(saveButton);
      saveButton.addEventListener('click', (event) =>{
        event.preventDefault();
        console.log('aici');
        // Get the updated values from the form
        const newEkgLow = document.getElementById('ekg_low').value;
        const newEkgHigh = document.getElementById('ekg_high').value;
        const newPulsLow = document.getElementById('puls_low').value;
        const newPulsHigh = document.getElementById('puls_high').value;
        const newTempLow = document.getElementById('temp_low').value;
        const newTempHigh = document.getElementById('temp_high').value;
        const newUmidLow = document.getElementById('umid_low').value;
        const newUmidHigh = document.getElementById('umid_high').value;
      
        try {
          onValue(uRef, (snapshot) => {
            const existingData = snapshot.val();
            const updatedData = {
              ...existingData,
              ekg_low: newEkgLow || existingData.ekg_low,
              ekg_high: newEkgHigh || existingData.ekg_high,
              puls_low: newPulsLow || existingData.puls_low,
              puls_high: newPulsHigh || existingData.puls_high,
              temp_low: newTempLow || existingData.temp_low,
              temp_high: newTempHigh || existingData.temp_high,
              umid_low: newUmidLow || existingData.umid_low,
              umid_high: newUmidHigh || existingData.umid_high
            };
            console.log('pdatede data: ' + updatedData)
            form.style.display = 'none';
            set(uRef, updatedData, (error) => {
              if (error) {
                console.log('Error saving user data:', error);
              } else {
                console.log('Data updated successfully!');
                // Close the form or perform any other actions
              }
            });
          });
        } catch (error) {
          console.log('Error saving user data:', error);
        }
      })
    });
  } else {
    // Form is currently shown, hide the form
    form.style.display = 'none';
    form.innerHTML = ''; // Clear the form content
  }
});