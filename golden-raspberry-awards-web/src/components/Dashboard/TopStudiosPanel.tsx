import React from "react";
import { Paper, Title, Table, LoadingOverlay } from "@mantine/core";
import { StudioWithWinCount } from "../../api";

interface TopStudiosPanelProps {
  studios: StudioWithWinCount[];
  isLoading: boolean;
}

const TopStudiosPanel: React.FC<TopStudiosPanelProps> = ({
  studios,
  isLoading,
}) => (
  <Paper shadow="xs" p="md" withBorder>
    <Title order={4} mb="md" c="#333">
      Top 3 studios with winners
    </Title>
    <LoadingOverlay visible={isLoading} />
    <Table striped highlightOnHover>
      <Table.Thead>
        <Table.Tr>
          <Table.Th>Name</Table.Th>
          <Table.Th>Win Count</Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>
        {studios.map((studio) => (
          <Table.Tr key={studio.name}>
            <Table.Td>{studio.name}</Table.Td>
            <Table.Td>{studio.winCount}</Table.Td>
          </Table.Tr>
        ))}
      </Table.Tbody>
    </Table>
  </Paper>
);

export default TopStudiosPanel;
