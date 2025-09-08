import React from "react";
import { Paper, Title, Table, LoadingOverlay } from "@mantine/core";
import { YearWithMultipleWinners } from "../../api";

interface YearsWithMultipleWinnersPanelProps {
  years: YearWithMultipleWinners[];
  isLoading: boolean;
}

const YearsWithMultipleWinnersPanel: React.FC<
  YearsWithMultipleWinnersPanelProps
> = ({ years, isLoading }) => (
  <Paper shadow="xs" p="md" withBorder>
    <Title order={4} mb="md" c="#333">
      Anos com múltiplos vencedores
    </Title>
    <LoadingOverlay visible={isLoading} />
    <Table striped highlightOnHover>
      <Table.Thead>
        <Table.Tr>
          <Table.Th>Ano</Table.Th>
          <Table.Th>Contagem de Vitórias</Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>
        {years.map((year) => (
          <Table.Tr key={year.year}>
            <Table.Td>{year.year}</Table.Td>
            <Table.Td>{year.winnerCount}</Table.Td>
          </Table.Tr>
        ))}
      </Table.Tbody>
    </Table>
  </Paper>
);

export default YearsWithMultipleWinnersPanel;
