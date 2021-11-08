import React from 'react';
import { Button, Grid, Paper } from '@mui/material';
import DropdownButton from './DropdownButton';
import { sendJson } from '../backend_conn';
import initValues from '../StaticValues';
import useStyles from '../Styles';

export default function DatacatPanel(props) {
    const classes = useStyles();
    const repository = props.repository;
    const triple = {
        relation: "",
        instance1: "",
        predicate1: "",
        instance2: "",
        predicate2: "",
    }

    const [elValue, setElValue] = React.useState(initValues);
    function handleCallback(id, item) {
        const newElValue = elValue.map((i, eid) => eid === id ? item : i);
        setElValue(newElValue);
    }

    function sendTriples(triple1) {
        const response = async () => {
            const res = await sendJson("POST", "/insertttobjrelation/repo/" + repository, triple1);
            console.log(res);
        }
        response();
    }

    return (
        <div className={classes.maxWidth}>
            <Grid container spacing={1} columns={8} className={classes.root}>
                <Grid item sm={2} >
                    <Paper className={classes.text}>
                        Semantische Verknüpfung von TT Objekten über TT Relations
                    </Paper>
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={3} elValue={elValue[3]} initValue="" repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={5} elValue={elValue[5]} initValue={elValue[6].value} repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={6} elValue={elValue[6]} initValue="" repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={7} elValue={elValue[7]} initValue={elValue[6].value} repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={8} elValue={elValue[8]} initValue="" repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <Button className={classes.button} onClick={() => {
                        triple.relation = elValue[6].value;
                        triple.instance1 = elValue[3].value;
                        triple.predicate1 = elValue[5].value;
                        triple.instance2 = elValue[8].value;
                        triple.predicate2 = elValue[7].value;
                        sendTriples(triple);
                    }}>Save</Button>
                </Grid>
            </Grid>
        </div >
    )
}