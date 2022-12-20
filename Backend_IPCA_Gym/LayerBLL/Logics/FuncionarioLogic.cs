using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    public class FuncionarioLogic
    {
        public static async Task<Response> GetFuncionariosLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Funcionario> funcionarioList = await FuncionarioService.GetAllService(sqlDataSource);

            if (funcionarioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(funcionarioList);
            }

            return response;
        }
    }
}
