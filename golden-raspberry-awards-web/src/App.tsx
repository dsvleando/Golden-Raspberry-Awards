import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { MantineProvider, createTheme } from '@mantine/core';
import { Notifications } from '@mantine/notifications';
import { Provider } from 'react-redux';
import { store } from './store';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import MoviesList from './pages/MoviesList';
import '@mantine/core/styles.css';
import '@mantine/notifications/styles.css';

const theme = createTheme({
  primaryColor: 'blue',
  defaultRadius: 'md',
  components: {
    Table: {
      defaultProps: {
        striped: true,
        highlightOnHover: true,
      },
    },
    Paper: {
      defaultProps: {
        shadow: 'xs',
        withBorder: true,
      },
    },
  },
});

function App() {
  return (
    <Provider store={store}>
      <MantineProvider theme={theme} forceColorScheme='light'>
        <Notifications />
        <Router>
          <Layout>
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/movies" element={<MoviesList />} />
            </Routes>
          </Layout>
        </Router>
      </MantineProvider>
    </Provider>
  );
}

export default App;