import React from 'react';
import { AppShell, NavLink, Text, Group, Stack } from '@mantine/core';
import { IconDashboard, IconList } from '@tabler/icons-react';
import { useLocation, useNavigate } from 'react-router-dom';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const location = useLocation();
  const navigate = useNavigate();

  const isDashboard = location.pathname === '/';
  const isMoviesList = location.pathname === '/movies';

  return (
    <AppShell
      header={{ height: 60 }}
      navbar={{ width: 200, breakpoint: 'sm' }}
      padding="md"
    >
      <AppShell.Header>
        <Group h="100%" px="md">
          <Text fw={600} size="lg">
            Golden Raspberry Awards
          </Text>
        </Group>
      </AppShell.Header>

      <AppShell.Navbar>
        <AppShell.Section p="md">
          <Stack gap="xs">
            <NavLink
              href="/"
              label="Painel de Controle"
              leftSection={<IconDashboard size="1rem" />}
              active={isDashboard}
              onClick={(e) => {
                e.preventDefault();
                navigate('/');
              }}
              style={{
                fontWeight: isDashboard ? 600 : 400,
                borderRadius: '4px',
              }}
            />
            <NavLink
              href="/movies"
              label="Lista de Filmes"
              leftSection={<IconList size="1rem" />}
              active={isMoviesList}
              onClick={(e) => {
                e.preventDefault();
                navigate('/movies');
              }}
              style={{
                fontWeight: isMoviesList ? 600 : 400,
                borderRadius: '4px',
              }}
            />
          </Stack>
        </AppShell.Section>
      </AppShell.Navbar>

      <AppShell.Main>
        {children}
      </AppShell.Main>
    </AppShell>
  );
};

export default Layout;